package databute.databutee.console;

import databute.databutee.Databutee;
import databute.databutee.DatabuteeConfiguration;
import databute.databutee.DatabuteeConstants;
import databute.databutee.bucket.Bucket;
import databute.databutee.entity.EntityType;
import databute.databutee.entity.request.EntityRequestMessage;
import databute.databutee.entity.request.EntityRequestType;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.commons.lang3.StringUtils;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

public final class Console {

    private static Console instance;

    private Databutee databutee;

    private final Terminal terminal;
    private final LineReader reader;

    private Console() throws IOException {
        this.terminal = TerminalBuilder.terminal();
        this.reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(new DefaultParser())
                .build();
    }

    private void start() {
        while (true) {
            final String line = reader.readLine(ConsoleConstants.PROMPT).trim();
            final ParsedLine parsedLine = reader.getParser().parse(line, 0);
            if (StringUtils.equalsAny(parsedLine.word(), "quit", "exit")) {
                break;
            } else if (StringUtils.equals(parsedLine.word(), "connect")) {
                if (databutee != null) {
                    terminal.writer().println("already connected.");
                    continue;
                }

                final List<InetSocketAddress> addresses = parsedLine.words().subList(1, parsedLine.words().size())
                        .stream()
                        .map(address -> {
                            final String[] hostnameAndPort = address.split(":");
                            if (hostnameAndPort.length == 1) {
                                final String hostname = hostnameAndPort[0];
                                return new InetSocketAddress(hostname, DatabuteeConstants.DEFAULT_PORT);
                            } else {
                                final String hostname = hostnameAndPort[0];
                                final int port = Integer.parseInt(hostnameAndPort[1]);
                                return new InetSocketAddress(hostname, port);
                            }
                        })
                        .collect(Collectors.toList());

                try {
                    databutee = new Databutee(new DatabuteeConfiguration() {
                        @Override
                        public EventLoopGroup loopGroup() {
                            return new NioEventLoopGroup();
                        }

                        @Override
                        public List<InetSocketAddress> addresses() {
                            return addresses;
                        }
                    });
                    databutee.connect();
                } catch (ConnectException e) {
                    terminal.writer().println("Failed to connect to " + addresses);

                    databutee = null;
                }
            } else if (StringUtils.equals(parsedLine.word(), "buckets")) {
                if (databutee == null) {
                    terminal.writer().println("not connected yet");
                } else {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Buckets").append(System.lineSeparator());
                    for (Bucket bucket : databutee.bucketGroup()) {
                        sb.append("\t").append(bucket).append(System.lineSeparator());
                    }
                    sb.append(System.lineSeparator());
                    terminal.writer().print(sb.toString());
                }
            } else if (StringUtils.equals(parsedLine.word(), "geti")) {
                if (databutee == null) {
                    terminal.writer().println("not connected yet");
                } else {
                    final String key = parsedLine.words().get(1);
                    final Integer value = Integer.parseInt(parsedLine.words().get(2));
                    final EntityRequestMessage entityRequestMessage = new EntityRequestMessage(EntityRequestType.GET, key, EntityType.INTEGER, value);
                    databutee.query(entityRequestMessage);
                }
            } else if (StringUtils.equals(parsedLine.word(), "getl")) {
                if (databutee == null) {
                    terminal.writer().println("not connected yet");
                } else {
                    final String key = parsedLine.words().get(1);
                    final Long value = Long.parseLong(parsedLine.words().get(2));
                    final EntityRequestMessage entityRequestMessage = new EntityRequestMessage(EntityRequestType.GET, key, EntityType.LONG, value);
                    databutee.query(entityRequestMessage);
                }
            } else if (StringUtils.equals(parsedLine.word(), "gets")) {
                if (databutee == null) {
                    terminal.writer().println("not connected yet");
                } else {
                    final String key = parsedLine.words().get(1);
                    final String value = parsedLine.words().get(2);
                    final EntityRequestMessage entityRequestMessage = new EntityRequestMessage(EntityRequestType.GET, key, EntityType.STRING, value);
                    databutee.query(entityRequestMessage);
                }
            } else if (StringUtils.equals(parsedLine.word(), "seti")) {
                if (databutee == null) {
                    terminal.writer().println("not connected yet");
                } else {
                    final String key = parsedLine.words().get(1);
                    final Integer value = Integer.parseInt(parsedLine.words().get(2));
                    final EntityRequestMessage entityRequestMessage = new EntityRequestMessage(EntityRequestType.SET, key, EntityType.INTEGER, value);
                    databutee.query(entityRequestMessage);
                }
            } else if (StringUtils.equals(parsedLine.word(), "setl")) {
                if (databutee == null) {
                    terminal.writer().println("not connected yet");
                } else {
                    final String key = parsedLine.words().get(1);
                    final Long value = Long.parseLong(parsedLine.words().get(2));
                    final EntityRequestMessage entityRequestMessage = new EntityRequestMessage(EntityRequestType.SET, key, EntityType.LONG, value);
                    databutee.query(entityRequestMessage);
                }
            } else if (StringUtils.equals(parsedLine.word(), "sets")) {
                if (databutee == null) {
                    terminal.writer().println("not connected yet");
                } else {
                    final String key = parsedLine.words().get(1);
                    final String value = parsedLine.words().get(2);
                    final EntityRequestMessage entityRequestMessage = new EntityRequestMessage(EntityRequestType.SET, key, EntityType.STRING, value);
                    databutee.query(entityRequestMessage);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            instance = new Console();
            instance.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
