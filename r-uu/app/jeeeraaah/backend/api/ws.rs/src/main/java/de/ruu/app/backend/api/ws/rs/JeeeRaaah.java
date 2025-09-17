package de.ruu.app.backend.api.ws.rs;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import static de.ruu.app.jeeeraaah.common.api.domain.Paths.PATH_TO_APP;

@ApplicationPath(PATH_TO_APP)
public class JeeeRaaah extends Application { }