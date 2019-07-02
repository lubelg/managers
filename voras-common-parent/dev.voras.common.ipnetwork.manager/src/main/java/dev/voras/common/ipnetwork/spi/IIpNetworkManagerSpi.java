package dev.voras.common.ipnetwork.spi;

import javax.validation.constraints.NotNull;

import dev.voras.ICredentials;
import dev.voras.common.ipnetwork.ICommandShell;
import dev.voras.common.ipnetwork.IpNetworkManagerException;

public interface IIpNetworkManagerSpi {

	@NotNull
	IIpHostSpi buildHost(String hostId) throws IpNetworkManagerException;

	@NotNull
	ICommandShell getCommandShell(String hostname, int port, ICredentials credentials) throws IpNetworkManagerException;

}