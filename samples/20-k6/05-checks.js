import { check } from 'k6';
import http from 'k6/http';

export default function () {
  const response = http.get('http://localhost:8080/api/v1/users/1');

  console.log('Response body text  : ' + response.body);
  console.log('Response body length: ' + response.body.length);

  check(response, {
    'status is 200': (r) => r.status === 200,
    'body includes user-01': (r) => r.body.includes('user-01'),
  });
}
