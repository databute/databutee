package databute.databutee.console;

import databute.databutee.Databutee;
import databute.databutee.DatabuteeConfiguration;
import databute.databutee.DatabuteeConstants;
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
                databutee.connect().join();
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
