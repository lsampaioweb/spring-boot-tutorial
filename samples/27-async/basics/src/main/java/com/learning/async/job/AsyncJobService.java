package com.learning.async.job;

import java.util.UUID;

interface AsyncJobService {

  AsyncJobResponse submit(AsyncJobRequest request);

  AsyncJobResponse find(UUID id);
}