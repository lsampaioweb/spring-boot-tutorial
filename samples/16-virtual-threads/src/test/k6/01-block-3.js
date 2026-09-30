import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  vus: 100,
  duration: '10s',
};

export default function () {
  const response = http.get(`${BASE_URL}/api/v1/httpbins/block/3`);

  check(response, {
    'Status:200': (r) => r.status === 200,
  });

  sleep(1);
}
