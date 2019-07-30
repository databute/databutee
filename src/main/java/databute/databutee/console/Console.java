package databute.databutee.console;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import databute.databutee.Callback;
import databute.databutee.Databutee;
import databute.databutee.DatabuteeConfiguration;
import databute.databutee.DatabuteeConstants;
import databute.databutee.bucket.Bucket;
import databute.databutee.entry.EmptyEntryKeyException;
import databute.databutee.entry.Entry;
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
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
            } else if (StringUtils.equals(parsedLine.word(), "get")) {
                if (databutee == null) {
                    terminal.writer().println("not connected yet");
                } else {
                    final String key = parsedLine.words().get(1);
                    try {
                        databutee.get(key, new Callback() {
                            @Override
                            public void onSuccess(Entry entry) {
                                terminal.writer().println(entry.toString());
                            }

                            @Override
                            public void onFailure(Exception e) {
                                terminal.writer().println(e.toString());
                            }
                        });
                    } catch (EmptyEntryKeyException e) {
                        terminal.writer().println("key must not be empty.");
                    }
                }
            } else if (StringUtils.equals(parsedLine.word(), "set")) {
                if (databutee == null) {
                    terminal.writer().println("not connected yet");
                } else {
                    final String type = parsedLine.words().get(1);

                    if (StringUtils.equals(type, "int")) {
                        final String key = parsedLine.words().get(2);
                        final Integer value = Integer.parseInt(parsedLine.words().get(3));
                        try {
                            databutee.setInteger(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    terminal.writer().println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    terminal.writer().println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            terminal.writer().println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "long")) {
                        final String key = parsedLine.words().get(2);
                        final Long value = Long.parseLong(parsedLine.words().get(3));
                        try {
                            databutee.setLong(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    terminal.writer().println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    terminal.writer().println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            terminal.writer().println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "string")) {
                        final String key = parsedLine.words().get(2);
                        final String value = parsedLine.words().get(3);
                        try {
                            databutee.setString(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    terminal.writer().println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    terminal.writer().println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            terminal.writer().println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "list")) {
                        final String key = parsedLine.words().get(2);
                        final List<String> value = parsedLine.words().subList(3, parsedLine.words().size());
                        try {
                            databutee.setList(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    terminal.writer().println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    terminal.writer().println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            terminal.writer().println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "set")) {
                        final String key = parsedLine.words().get(2);
                        final List<String> items = parsedLine.words().subList(3, parsedLine.words().size());
                        final Set<String> value = Sets.newHashSet(items);
                        try {
                            databutee.setSet(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    terminal.writer().println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    terminal.writer().println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            terminal.writer().println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "dict")) {
                        final String key = parsedLine.words().get(2);
                        final List<String> items = parsedLine.words().subList(3, parsedLine.words().size());
                        final Iterator<String> itemIterator = items.iterator();

                        final Map<String, String> value = Maps.newHashMap();
                        while (itemIterator.hasNext()) {
                            final String itemKey = itemIterator.next();
                            final String item = itemIterator.next();

                            value.put(itemKey, item);
                        }
                        try {
                            databutee.setDictionary(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    terminal.writer().println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    terminal.writer().println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            terminal.writer().println("key must not be empty.");
                        }
                    }
                }
            } else if (StringUtils.equals(parsedLine.word(), "update")) {
                if (databutee == null) {
                    terminal.writer().println("not connected yet");
                } else {
                    final String type = parsedLine.words().get(1);

                    if (StringUtils.equals(type, "int")) {
                        final String key = parsedLine.words().get(2);
                        final Integer value = Integer.parseInt(parsedLine.words().get(3));
                        try {
                            databutee.updateInteger(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    terminal.writer().println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    terminal.writer().println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            terminal.writer().println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "long")) {
                        final String key = parsedLine.words().get(2);
                        final Long value = Long.parseLong(parsedLine.words().get(3));
                        try {
                            databutee.updateLong(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    terminal.writer().println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    terminal.writer().println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            terminal.writer().println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "string")) {
                        final String key = parsedLine.words().get(2);
                        final String value = parsedLine.words().get(3);
                        try {
                            databutee.updateString(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    terminal.writer().println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    terminal.writer().println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            terminal.writer().println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "list")) {
                        final String key = parsedLine.words().get(2);
                        final List<String> value = parsedLine.words().subList(3, parsedLine.words().size());
                        try {
                            databutee.updateList(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    terminal.writer().println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    terminal.writer().println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            terminal.writer().println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "set")) {
                        final String key = parsedLine.words().get(2);
                        final List<String> items = parsedLine.words().subList(3, parsedLine.words().size());
                        final Set<String> value = Sets.newHashSet(items);
                        try {
                            databutee.updateSet(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    terminal.writer().println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    terminal.writer().println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            terminal.writer().println("key must not be empty.");
                        }
                    } else if (StringUtils.equals(type, "dict")) {
                        final String key = parsedLine.words().get(2);
                        final List<String> items = parsedLine.words().subList(3, parsedLine.words().size());
                        final Iterator<String> itemIterator = items.iterator();

                        final Map<String, String> value = Maps.newHashMap();
                        while (itemIterator.hasNext()) {
                            final String itemKey = itemIterator.next();
                            itemIterator.remove();
                            final String item = itemIterator.next();
                            itemIterator.remove();

                            value.put(itemKey, item);
                        }
                        try {
                            databutee.updateDictionary(key, value, new Callback() {
                                @Override
                                public void onSuccess(Entry entry) {
                                    terminal.writer().println(entry.toString());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    terminal.writer().println(e.toString());
                                }
                            });
                        } catch (EmptyEntryKeyException e) {
                            terminal.writer().println("key must not be empty.");
                        }
                    }
                }
            } else if (StringUtils.equals(parsedLine.word(), "delete")) {
                if (databutee == null) {
                    terminal.writer().println("not connected yet");
                } else {
                    final String key = parsedLine.words().get(1);
                    try {
                        databutee.delete(key, new Callback() {
                            @Override
                            public void onSuccess(Entry entry) {
                                terminal.writer().println(entry.toString());
                            }

                            @Override
                            public void onFailure(Exception e) {
                                terminal.writer().println(e.toString());
                            }
                        });
                    } catch (EmptyEntryKeyException e) {
                        terminal.writer().println("key must not be empty.");
                    }
                }
            } else if (StringUtils.equals(parsedLine.word(), "expire")) {
                if (databutee == null) {
                    terminal.writer().println("not connected yet");
                } else {
                    final String key = parsedLine.words().get(1);
                    final long expireAfter = Long.parseLong(parsedLine.words().get(2));
                    final Instant expireAt = Instant.now().plusSeconds(expireAfter);
                    try {
                        databutee.expire(key, expireAt, new Callback() {
                            @Override
                            public void onSuccess(Entry entry) {
                                terminal.writer().println(entry.toString());
                            }

                            @Override
                            public void onFailure(Exception e) {
                                terminal.writer().println(e.toString());
                            }
                        });
                    } catch (EmptyEntryKeyException e) {
                        terminal.writer().println("key must not be empty.");
                    }
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
