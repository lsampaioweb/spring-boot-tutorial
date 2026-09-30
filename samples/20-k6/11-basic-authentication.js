import encoding from 'k6/encoding';
import http from 'k6/http';
import { check } from 'k6';

const BASE_URL = 'http://localhost:8080/api/v1/security/profile';
const username = `${__ENV.SECURITY_USER_USERNAME}`;
const password = `${__ENV.SECURITY_USER_PASSWORD}`;

export default function () {
  const credentials = `${username}:${password}`;

  const url = `http://${credentials}@localhost:8080/api/v1/security/profile`;
  let response = http.get(url);

  check(response, {
    'status is 200': (r) => r.status === 200,
    'body includes username': (r) => r.body.includes(username),
  });

  const encodedCredentials = encoding.b64encode(credentials);
  const options = {
    headers: {
      Authorization: `Basic ${encodedCredentials}`,
    },
  };

  response = http.get(BASE_URL, options);

  check(response, {
    'status is 200 with header': (r) => r.status === 200,
    'body includes username with header': (r) => r.body.includes(username),
  });
}
