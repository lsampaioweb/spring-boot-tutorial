import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 10,
  duration: '30s',
};

export default function () {
  http.get('http://jump-server-01.lan.homelab:8080/api/v1/users');

  sleep(1);
}
