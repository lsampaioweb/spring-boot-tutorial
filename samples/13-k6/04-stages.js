import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '5s', target: 20 },
    { duration: '1m5s', target: 10 },
    { duration: '10s', target: 1 },
  ],
};

export default function () {
  const response = http.get('http://jump-server-01.homelab:8080/api/v1/users');

  check(response, {
    'status was 200': (r) => r.status === 200,
  });

  sleep(1);
}
