import http from 'k6/http';

export function setup() {
  const res = http.get('http://jump-server-01.homelab:8080/api/v1/users');

  return { data: res.json() };
}

export default function (data) {
  console.log("Default:" + JSON.stringify(data));
}

export function teardown(data) {
  console.log("Teardown:" + JSON.stringify(data));
}
