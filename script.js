import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';

// Custom metrics
export let errorRate = new Rate('errors');
export let responseTime = new Trend('response_time');

// Test configuration
export let options = {
  stages: [
    { duration: '10s', target: 50 },   // Ramp up to 50 VUs over 10 seconds
    { duration: '50s', target: 100 },  // Ramp up to 100 VUs over 50 seconds  
    { duration: '10s', target: 0 },    // Ramp down to 0 VUs over 10 seconds
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% of requests must be below 200ms
    errors: ['rate<0.01'],            // Error rate must be below 1% (practically 0)
    http_req_failed: ['rate<0.01'],   // Failed requests must be below 1%
  },
};

// Base URL
const BASE_URL = 'http://localhost:8080';

export default function () {
  // Test GET /api/patients
  let response = http.get(`${BASE_URL}/api/patients`, {
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    timeout: '10s', // Set timeout to prevent hanging requests
  });

  // Check response
  let result = check(response, {
    'status is 200': (r) => r.status === 200,
    'response time < 500ms': (r) => r.timings.duration < 500,
    'response has body': (r) => r.body && r.body.length > 0,
    'content type is JSON': (r) => r.headers['Content-Type'] && r.headers['Content-Type'].includes('application/json'),
  });

  // Record custom metrics
  errorRate.add(!result);
  responseTime.add(response.timings.duration);

  // Add small delay between requests to simulate realistic user behavior
//   sleep(1);
}

// Setup function (runs once before the test)
export function setup() {
  console.log('Starting K6 Performance Test for Patients API');
  console.log('Target: http://localhost:8080/api/patients');
  console.log('Scenario: 10s->50VU, 50s->100VU, 10s->0VU');
  console.log('Requirements: Response time < 500ms, Error rate = 0%');
  
  // Optional: Add a warm-up request to check if API is available
  let warmupResponse = http.get(`${BASE_URL}/api/patients`);
  if (warmupResponse.status !== 200) {
    console.warn(`Warning: API returned status ${warmupResponse.status} during setup`);
  }
}

// Teardown function (runs once after the test)
export function teardown(data) {
  console.log('K6 Performance Test completed');
}