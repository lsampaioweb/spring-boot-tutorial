import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 100,
  duration: '1m',
  thresholds: {
    // 95% of the responses should be faster than 500ms.
    http_req_duration: ['p(95) < 500'],

    // 99% of static assets should load in less than 150ms.
    'http_req_duration{staticAsset:yes}': ['p(99) < 150'],

    // Average response time should be less than 200ms, with 95% of responses under 400ms.
    'http_req_duration{staticAsset:no}': ['avg < 200', 'p(95) < 400'],
  },
};

export default function () {
  const response = http.get('http://jump-server-01.lan.homelab:8080/api/v1/users/1');

  check(response, {
    'Status is 200': (r) => r.status === 200,
  });

  sleep(1);
}
