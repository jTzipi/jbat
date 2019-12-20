package earth.eu.jtzipi.jbat;

import earth.eu.jtzipi.modules.io.watcher.IWatchEventHandler;

import java.nio.file.Path;

public class RootWatchEventListener implements IWatchEventHandler {

    @Override
    public EventAction onOverflow( Path path, int i ) {
        return null;
    }

    @Override
    public EventAction onCreate( Path path, int i ) {
        return null;
    }

    @Override
    public EventAction onModify( Path path, int i ) {
        return null;
    }

    @Override
    public EventAction onDelete( Path path, int i ) {
        return null;
    }
}
