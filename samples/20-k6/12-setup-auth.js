import encoding from 'k6/encoding';
import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = 'http://localhost:8080/api/v1/security/profile';

export const options = {
  vus: 10,
  duration: '10s',
  thresholds: {
    http_req_failed: ['rate < 0.01'],
    http_req_duration: ['p(95) < 250']
  }
}

export function setup() {
  const username = `${__ENV.SECURITY_USER_USERNAME}`;
  const password = `${__ENV.SECURITY_USER_PASSWORD}`;
  const authorization = `Basic ${encoding.b64encode(`${username}:${password}`)}`;

  const response = http.get(BASE_URL, {
    headers: {
      Authorization: authorization,
    },
  });

  check(response, {
    'setup authenticated': (r) => r.status === 200,
  });

  return { authorization, username };
}

export default function (data) {
  const response = http.get(BASE_URL, {
    headers: {
      Authorization: data.authorization,
    },
  });

  check(response, {
    'status code 200': (r) => r.status === 200,
    'body includes username': (r) => r.body.includes(data.username),
  });

  sleep(1);
}
