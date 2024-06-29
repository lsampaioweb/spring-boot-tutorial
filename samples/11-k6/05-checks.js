import { check } from 'k6';
import http from 'k6/http';

export default function () {
  const response = http.get('http://jump-server-01.homelab:8080/api/v1/users/1');

  console.log('Response body text  : ' + response.body);
  console.log('Response body length: ' + response.body.length);

  check(response, {
    'Verify homepage text': (r) => r.body.includes('user-01'),
    'body size is 49 bytes': (r) => r.body.length == 49,
  });
}
