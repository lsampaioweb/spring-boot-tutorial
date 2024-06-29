import { SharedArray } from 'k6/data';
import { scenario } from 'k6/execution';

// Not using SharedArray here will mean that the code in the function call (that is what loads and parses the json) will be executed per each VU which also means that there will be a complete copy per each VU.

const data = new SharedArray('some data name', function () {
  return JSON.parse(open('./data/data.json')).users;
});

export const options = {
  scenarios: {
    'use-all-the-data': {
      executor: 'shared-iterations',
      vus: 1,
      iterations: data.length,
      maxDuration: '1h',
    },
  },
};

export default function () {
  const user = data[0];

  console.log(`User 01: ${JSON.stringify(user)}`);

  // This is unique even in the cloud.
  const user2 = data[scenario.iterationInTest];

  console.log(`User 02: ${JSON.stringify(user2)}`);
}
