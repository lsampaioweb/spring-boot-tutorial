import http from 'k6/http';
import { check, sleep } from 'k6';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

const BASE_URL = 'http://jump-server-01.lan.homelab:8080';

export const options = {
  stages: [
    { duration: '5s', target: 10 },
    { duration: '5s', target: 20 },
    { duration: '5s', target: 30 },

    { duration: '5s', target: 40 },

    { duration: '5s', target: 30 },
    { duration: '5s', target: 20 },
    { duration: '5s', target: 10 },
  ]
};

export default function () {
  const response = http.get(`${BASE_URL}/httpbin/block/3`);

  check(response, {
    'Status:200': (r) => r.status === 200,
  });

  sleep(1);
}

export function handleSummary(data) {
  return {
    "output/virtual-threads.html": htmlReport(data),
  };
}
