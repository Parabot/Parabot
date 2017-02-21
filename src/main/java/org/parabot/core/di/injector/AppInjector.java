package org.parabot.core.di.injector;

import com.google.inject.AbstractModule;
import org.parabot.core.bdn.api.servers.IServerDownloader;
import org.parabot.core.bdn.api.servers.ServerDownloader;
import org.parabot.core.user.SharedUserAuthenticator;
import org.parabot.core.user.UserAuthenticator;

public class AppInjector extends AbstractModule {

	@Override
    protected void configure() {
		bind(SharedUserAuthenticator.class).to(UserAuthenticator.class);
		bind(IServerDownloader.class).to(ServerDownloader.class);
	}

}