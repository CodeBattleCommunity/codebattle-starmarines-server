package com.epam.game.gameinfrastructure.requessthandling;

import com.epam.game.gamemodel.model.GameInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Garin on 09.08.2014.
 */
public class SocketReaper implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SocketReaper.class.getName());

    private long cleanupTimeout = 4000;

    private Map<GameInstance, Set<PeerController>> pool;
    private boolean stopped;

    public SocketReaper(Map<GameInstance, Set<PeerController>> pool, long timeout) {
        this.pool = pool;
    }

    public void stop() {
        this.stopped = true;
    }

    public void setCleanupTimeout(long timeout) {
        cleanupTimeout = timeout;
    }

    @Override
    public void run() {
        byte[] dummy = new byte[1];
        log.info("Socket cleaner thread started.");
        while (!stopped) {
            try {
                Thread.sleep(cleanupTimeout);

                synchronized (pool) {
                    long time = System.currentTimeMillis();
                    for (Map.Entry<GameInstance, Set<PeerController>> entry : pool.entrySet()) {
                        log.info("Check for game " + entry.getKey().getTitle() + ", game is started: " + entry.getKey().isStarted());
                        if (!entry.getKey().isStarted()) {
                            synchronized (entry.getKey()) {
                                // the game might be started during lock acquiring
                                if (!entry.getKey().isStarted()) {
                                    Set<PeerController> peerControllers = entry.getValue();
                                    for (Iterator<PeerController> i = peerControllers.iterator(); i.hasNext();) {
                                        PeerController controller = i.next();
                                        log.info("Checking sockets for user {}", controller.getUser().getId());
                                        try {
                                            controller.getSocket().setSoTimeout(3);
                                            try {
                                                // if client is still there, this call is supposed to throw timeout exception
                                                controller.getSocket().getInputStream().read(dummy,0,1);
                                                // though this hack might possibly lead to a ridiculous thing:
                                                log.error("User {} ({}) ACTUALLY SENT something when he wasn't supposed to, and it was received in 1ms. Probably the user has erroneous code.", controller.getUser().getId(), controller.getUser().getEmail());
                                            } catch (SocketTimeoutException e) {
                                                // ok, the user is still there
                                                log.info("Ok, the user is waiting");
                                                controller.getSocket().setSoTimeout(0);
                                            }
                                        }  catch (Exception e) {
                                            // the user is dead, drop the connection
                                            log.info( "The user appears to be dead.");
                                            i.remove();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    log.info("All the checks took " + (System.currentTimeMillis() - time) + "ms");

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
