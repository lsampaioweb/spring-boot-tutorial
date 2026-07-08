package com.learning.traefik.user;

import java.net.UnknownHostException;

interface HostInfoService {

  HelloResponse getHostInfo() throws UnknownHostException;
}
