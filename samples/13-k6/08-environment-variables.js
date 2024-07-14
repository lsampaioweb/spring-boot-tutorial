import http from 'k6/http';
import { check, sleep } from 'k6';

export default function () {
  const response = http.get(`${__ENV.MY_HOSTNAME}`);

  check(response, {
    'status was 200': (r) => r.status === 200,
  });

  sleep(1);
}
