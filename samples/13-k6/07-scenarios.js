import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  scenarios: {
    my_web_test: {
      // The function this scenario will execute.
      exec: 'webtest',
      executor: 'constant-vus',
      vus: 50,
      duration: '1m',
    },
  },
};

export function webtest() {
  http.get('http://jump-server-01.lan.homelab:8080/api/v1/users');

  sleep(Math.random() * 2);
}
