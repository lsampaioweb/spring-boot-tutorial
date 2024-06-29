// init
  // Load local files, import modules, declare lifecycle functions.
  // Open JSON file, Import module.
  // Once per VU.

export function setup() {
  // Setup
    // Set up data for processing, share data among VUs.
    // Call API to start test environment.
    // Once.
  }

  export default function (data) {
  // VU code
    // Run the test function, usually default
    // Make https requests, validate responses
    // Once per iteration, as many times as the test options require.
  }

  export function teardown(data) {
  // Teardown
    // Process result of setup code, stop test environment
    // Validate that setup had a certain result, send webhook notifying that test has finished
    // Once
    // If the Setup function ends abnormally (e.g throws an error), the teardown() function isn’t called. Consider adding logic to the setup() function to handle errors and ensure proper cleanup.
  }
