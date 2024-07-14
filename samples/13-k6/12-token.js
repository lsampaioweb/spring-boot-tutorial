import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = 'https://test-api.k6.io';

export const options = {
  vus: 10,
  duration: '10s',
  thresholds: {
    http_req_failed: ['rate < 0.01'],
    http_req_duration: ['p(95) < 250']
  }
}

export function setup() {
  const loginRes = http.post(`${BASE_URL}/auth/token/login/`, {
    username: '0.5577608357004727@mail.com',
    // password: 'user123'
    password: `${__ENV.PASSWORD}`
  });

  const token = loginRes.json('access');

  return token;
}

export default function (token) {

  const params = {
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  }
  const response = http.get(`${BASE_URL}/my/crocodiles`, params);

  check(response, {
    'status code 200': (r) => r.status === 200
  });

  sleep(1)
}
