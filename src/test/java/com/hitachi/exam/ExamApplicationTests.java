package com.hitachi.exam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitachi.exam.entity.ParkingLot;
import com.hitachi.exam.entity.Vehicle;
import com.hitachi.exam.repository.ParkedVehicleRepository;
import com.hitachi.exam.repository.ParkingLotRepository;
import com.hitachi.exam.repository.VehicleRepository;
import com.hitachi.exam.repository.VehicleTypeRepository;
import com.hitachi.exam.request.CreateParkingLotRequest;
import com.hitachi.exam.request.CreateVehicleRequest;
import com.hitachi.exam.request.LoginRequest;
import com.hitachi.exam.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExamApplicationTests {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	ParkedVehicleRepository parkedVehicleRepository;

	@Autowired
	ParkingLotRepository parkingLotRepository;

	@Autowired
	VehicleRepository vehicleRepository;

	@Autowired
	VehicleTypeRepository vehicleTypeRepository;

	@BeforeEach
	void clearDatabase() {
		parkedVehicleRepository.deleteAll();
		parkingLotRepository.deleteAll();
		vehicleRepository.deleteAll();
	}

	@Test
	void loginWithValidCredentialsShouldReturnJwt() throws Exception {
		var loginRequest = new LoginRequest("user", "password");

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists());
	}

	@Test
	void loginWithInvalidCredentialsShouldReturn401() throws Exception {
		var loginRequest = new LoginRequest("user", "123");

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void registerParkingLotSuccess() throws Exception {
		String token = jwtUtils.generateToken("user");

		var parkingLot = new CreateParkingLotRequest("MKT01", "Makati", 10, 5);

		mockMvc.perform(post("/api/parking")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(parkingLot)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.lotId").value("MKT01"))
				.andExpect(jsonPath("$.location").value("Makati"))
				.andExpect(jsonPath("$.capacity").value(10))
				.andExpect(jsonPath("$.occupiedSpaces").value(0))
				.andExpect(jsonPath("$.costPerMinute").value(5.0));
	}

	@Test
	void registerParkingLotDuplicate() throws Exception {
		parkingLotRepository.save(new ParkingLot("MKT01", "Makati", 10, 0, 5.0));

		String token = jwtUtils.generateToken("user");

		var parkingLot = new CreateParkingLotRequest("MKT01", "Makati", 10, 5.0);

		mockMvc.perform(post("/api/parking")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(parkingLot)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.statusCode").value(400))
				.andExpect(jsonPath("$.message").value("Parking lot already exists"));
	}

	@Test
	void registerParkingLotEmptyLotId() throws Exception {
		String token = jwtUtils.generateToken("user");

		var parkingLot = new CreateParkingLotRequest("", "Makati", 10, 5.0);

		mockMvc.perform(post("/api/parking")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(parkingLot)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.statusCode").value(400))
				.andExpect(jsonPath("$.message").value("Id is required"));
	}

	@Test
	void registerParkingLotZeroCapacity() throws Exception {
		String token = jwtUtils.generateToken("user");

		var parkingLot = new CreateParkingLotRequest("MKT01", "Makati", 0, 5.0);

		mockMvc.perform(post("/api/parking")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(parkingLot)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.statusCode").value(400))
				.andExpect(jsonPath("$.message").value("Parking lot capacity must be greater than 0"));
	}

	@Test
	void registerVehicleSuccess() throws Exception {
		String token = jwtUtils.generateToken("user");

		var vehicle = new CreateVehicleRequest("ABC-123", "car", "Erick");

		mockMvc.perform(post("/api/vehicle")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(vehicle)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.licensePlate").value("ABC-123"))
				.andExpect(jsonPath("$.vehicleType.type").value("car"))
				.andExpect(jsonPath("$.ownerName").value("Erick"));
	}

	@Test
	void registerVehicleDuplicate() throws Exception {
		var carType = vehicleTypeRepository.findByType("car").get();
		vehicleRepository.save(new Vehicle("ABC-123", carType, "Erick"));

		String token = jwtUtils.generateToken("user");

		var vehicle = new CreateVehicleRequest("ABC-123", "car", "Erick");

		mockMvc.perform(post("/api/vehicle")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(vehicle)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.statusCode").value(400))
				.andExpect(jsonPath("$.message").value("Vehicle already exists"));
	}

	@Test
	void registerVehicleEmptyLicensePlate() throws Exception {
		String token = jwtUtils.generateToken("user");

		var vehicle = new CreateVehicleRequest("", "car", "Erick");

		mockMvc.perform(post("/api/vehicle")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(vehicle)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.statusCode").value(400))
				.andExpect(jsonPath("$.message").value("License plate is required"));
	}

	@Test
	void registerVehicleNullLicensePlate() throws Exception {
		String token = jwtUtils.generateToken("user");

		var vehicle = new CreateVehicleRequest(null, "car", "Erick");

		mockMvc.perform(post("/api/vehicle")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(vehicle)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.statusCode").value(400))
				.andExpect(jsonPath("$.message").value("License plate is required"));
	}

	@Test
	void registerVehicleInvalidVehicleType() throws Exception {
		String token = jwtUtils.generateToken("user");

		var vehicle = new CreateVehicleRequest("ABC-123", "bicycle", "Erick");

		mockMvc.perform(post("/api/vehicle")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(vehicle)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.statusCode").value(400))
				.andExpect(jsonPath("$.message").value("Invalid vehicle type"));
	}

	@Test
	void parkingLotAvailabilityInitial() throws Exception {
		parkingLotRepository.save(new ParkingLot("MKT01", "Makati", 10, 0, 5.0));

		String token = jwtUtils.generateToken("user");


		mockMvc.perform(get("/api/parking/availability")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.param("id", "MKT01"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.lotId").value("MKT01"))
				.andExpect(jsonPath("$.capacity").value(10))
				.andExpect(jsonPath("$.occupiedSpaces").value(0))
				.andExpect(jsonPath("$.available").value(10));
	}

	@Test
	void parkingLotAvailabilityParkingNotExists() throws Exception {
		String token = jwtUtils.generateToken("user");


		mockMvc.perform(get("/api/parking/availability")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.param("id", "MKT01"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.statusCode").value(400))
				.andExpect(jsonPath("$.message").value("Parking lot not found"));
	}
}
