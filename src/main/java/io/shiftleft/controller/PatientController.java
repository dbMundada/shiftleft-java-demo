package io.shiftleft.controller;

import io.shiftleft.data.DataLoader;
import io.shiftleft.model.Patient;
import io.shiftleft.repository.PatientRepository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Admin checks login
 */
@RestController
public class PatientController {

    private static Logger log = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private JedisPool jedisPool;

    /**
     * Gets all patients.
     *
     * @return the patients
     */
    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    public List<Patient> getPatients(String userId, HttpServletResponse response) {
        // Check if the userId cookie exists
        if (userId != null) {
            log.info("User ID from cookie: {}", userId);
        } else {
            // If the userId cookie doesn't exist, generate a new one and set it in the response
            userId = generateUserId();
            Cookie cookie = new Cookie("userId", userId);
            response.addCookie(cookie);
            log.info("Generated new User ID: {}", userId);
        }

        // Attempt to retrieve patients from Redis cache
        List<Patient> patients = retrievePatientsFromCache();

        if (patients == null) {
            // If patients are not found in the cache, retrieve them from the database
            patients = patientRepository.findAll();

            // Store patients in Redis cache for future use
            storePatientsInCache(patients);
        } else {
            log.info("Retrieved patients from cache");
        }

        return patients;
    }

    /**
     * Get a specific patient by ID.
     *
     * @param patientId the ID of the patient
     * @return the patient
     */
    @RequestMapping(value = "/patients/{patientId}", method = RequestMethod.GET)
    public Patient getPatient(@PathVariable("patientId") Long patientId, String userId,
                              HttpServletResponse response) {
        // Check if userId cookie is present
        if (userId != null) {
            // Attempt to retrieve patient from Redis cache
            Patient patient = retrievePatientFromCache(patientId);
            if (patient != null) {
                log.info("Patient {} retrieved from cache", patientId);
                return patient;
            }
        }

        // Retrieve patient from the database
        Patient patient = patientRepository.findOne(patientId);

        // Store patient in Redis cache
        storePatientInCache(patientId, patient);

        // Set userId cookie in the response
        response.addCookie(new Cookie("userId", "123456"));

        return patient;
    }

    /**
     * Update a specific patient.
     *
     * @param patientId the ID of the patient to update
     * @param patientObj the updated patient object
     * @return the updated patient
     */
    @RequestMapping(value = "/patients/{patientId}", method = RequestMethod.PUT)
    public Patient updatePatient(@PathVariable("patientId") Long patientId,
                                 @RequestBody Patient patientObj) {
        Patient existingPatient = patientRepository.findOne(patientId);
        if (existingPatient != null) {
            // Update the existing patient with the updated fields
            existingPatient.setName(patientObj.getName());
            existingPatient.setAge(patientObj.getAge());
            // ... (update other fields as needed)

            // Save the updated patient to the database
            Patient savedPatient = patientRepository.save(existingPatient);

            // Update the patient in the Redis cache
            updatePatientInCache(patientId, savedPatient);

            return savedPatient;
        } else {
            // Patient with the specified ID not found
            throw new IllegalArgumentException("Patient not found");
        }
    }

    private String generateUserId() {
        // Logic to generate a unique user ID
        return "12345";
    }

    private List<Patient> retrievePatientsFromCache() {
        // Key to use for storing patients in Redis cache
        String cacheKey = "patients";

        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.exists(cacheKey)) {
                // Patients found in the cache
                String json = jedis.get(cacheKey);
                return deserializePatients(json);
            }
        } catch (JedisException e) {
            log.error("Error retrieving patients from Redis cache: {}", e.getMessage());
        }

        // Patients not found in the cache
        return null;
    }

    private void storePatientsInCache(List<Patient> patients) {
        // Key to use for storing patients in Redis cache
        String cacheKey = "patients";

        try (Jedis jedis = jedisPool.getResource()) {
            String json = serializePatients(patients);
            jedis.set(cacheKey, json);
        } catch (JedisException e) {
            log.error("Error storing patients in Redis cache: {}", e.getMessage());
        }
    }

    private Patient retrievePatientFromCache(Long patientId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String patientJson = jedis.get("patient:" + patientId);
            if (patientJson != null) {
                return deserializePatient(patientJson);
            }
        } catch (JedisException e) {
            log.error("Error retrieving patient from cache: {}", e.getMessage());
        }
        return null;
    }

    private void storePatientInCache(String dateOfBirth, Patient patient) {
        try (Jedis jedis = jedisPool.getResource()) {
            String patientJson = serializePatient(patient);
            jedis.set("patient:" + dateOfBirth, patientJson);
        } catch (JedisException e) {
            log.error("Error storing patient in cache: {}", e.getMessage());
        }
    }

    private void updatePatientInCache(String dateOfBirth, Patient patient) {
        try (Jedis jedis = jedisPool.getResource()) {
            String patientJson = serializePatient(patient);
            jedis.set("patient:" + dateOfBirth, patientJson);
        } catch (JedisException e) {
            log.error("Error updating patient in cache: {}", e.getMessage());
        }
    }

    private String serializePatients(List<Patient> patients) {
        // Logic to serialize patients to JSON
        // Implement your preferred JSON serialization library or use a custom implementation
        return ""; // Replace with actual serialization logic
    }

    private List<Patient> deserializePatients(String json) {
        // Logic to deserialize patients from JSON
        // Implement your preferred JSON deserialization library or use a custom implementation
        return null; // Replace with actual deserialization logic
    }

    private Patient deserializePatient(String patientJson) {
        // Implement deserialization logic here
        return null;
    }

    private String serializePatient(Patient patient) {
        // Implement serialization logic here
        return null;
    }

}
