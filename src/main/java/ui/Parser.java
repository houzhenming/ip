package ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {

    public enum CommandType { BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, UNKNOWN }

    public static class ParsedCommand {
        public final CommandType type;
        public final String desc;           // for TODO / DEADLINE / EVENT
        public final Integer index;         // for MARK / UNMARK / DELETE (1-based from user)
        public final LocalDateTime from;    // for EVENT
        public final LocalDateTime to;      // for EVENT
        public final LocalDateTime by;      // for DEADLINE

        private ParsedCommand(CommandType type, String desc, Integer index,
                              LocalDateTime from, LocalDateTime to, LocalDateTime by) {
            this.type = type;
            this.desc = desc;
            this.index = index;
            this.from = from;
            this.to = to;
            this.by = by;
        }

        public static ParsedCommand bye() { return new ParsedCommand(CommandType.BYE, null, null, null, null, null); }
        public static ParsedCommand list() { return new ParsedCommand(CommandType.LIST, null, null, null, null, null); }
        public static ParsedCommand mark(int idx) { return new ParsedCommand(CommandType.MARK, null, idx, null, null, null); }
        public static ParsedCommand unmark(int idx) { return new ParsedCommand(CommandType.UNMARK, null, idx, null, null, null); }
        public static ParsedCommand del(int idx) { return new ParsedCommand(CommandType.DELETE, null, idx, null, null, null); }
        public static ParsedCommand todo(String d) { return new ParsedCommand(CommandType.TODO, d, null, null, null, null); }
        public static ParsedCommand deadline(String d, LocalDateTime by) {
            return new ParsedCommand(CommandType.DEADLINE, d, null, null, null, by);
        }
        public static ParsedCommand event(String d, LocalDateTime from, LocalDateTime to) {
            return new ParsedCommand(CommandType.EVENT, d, null, from, to, null);
        }
        public static ParsedCommand unknown() { return new ParsedCommand(CommandType.UNKNOWN, null, null, null, null, null); }
    }

    private final DateTimeFormatter formatter; // use "yyyy-MM-dd HH:mm" (no 'T')

    public Parser(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public ParsedCommand parse(String input) {
        if (input == null) return ParsedCommand.unknown();
        String s = input.trim();
        if (s.isEmpty()) return ParsedCommand.unknown();

        String lower = s.toLowerCase();

        if (lower.equals("bye"))  return ParsedCommand.bye();
        if (lower.equals("list")) return ParsedCommand.list();

        if (lower.startsWith("mark"))   return ParsedCommand.mark(extractIndex(s, "mark"));
        if (lower.startsWith("unmark")) return ParsedCommand.unmark(extractIndex(s, "unmark"));
        if (lower.startsWith("delete")) return ParsedCommand.del(extractIndex(s, "delete"));

        if (lower.startsWith("todo")) {
            String desc = s.substring(4).trim();
            if (desc.isEmpty()) throw new IllegalArgumentException("todo description cannot be empty");
            return ParsedCommand.todo(desc);
        }

        if (lower.startsWith("deadline")) {
            // format: deadline DESC /by 2007-10-02 13:45
            String[] parts = s.substring(8).split("/by", 2);
            if (parts.length < 2) throw new IllegalArgumentException("deadline requires '/by <yyyy-MM-dd HH:mm>'");
            String desc = parts[0].trim();
            LocalDateTime by = parseDT(parts[1].trim());
            if (desc.isEmpty()) throw new IllegalArgumentException("deadline description cannot be empty");
            return ParsedCommand.deadline(desc, by);
        }

        if (lower.startsWith("event")) {
            // format: event DESC /from 2007-10-02 13:45 /to 2007-10-02 15:00
            String[] p1 = s.substring(5).split("/from", 2);
            if (p1.length < 2) throw new IllegalArgumentException("event requires '/from <yyyy-MM-dd HH:mm>'");
            String desc = p1[0].trim();

            String[] p2 = p1[1].split("/to", 2);
            if (p2.length < 2) throw new IllegalArgumentException("event requires '/to <yyyy-MM-dd HH:mm>'");
            LocalDateTime from = parseDT(p2[0].trim());
            LocalDateTime to   = parseDT(p2[1].trim());

            if (desc.isEmpty()) throw new IllegalArgumentException("event description cannot be empty");
            if (to.isBefore(from)) throw new IllegalArgumentException("event '/to' must be after '/from'");
            return ParsedCommand.event(desc, from, to);
        }

        return ParsedCommand.unknown();
    }

    private int extractIndex(String s, String cmd) {
        String[] tokens = s.split("\\s+");
        if (tokens.length < 2)
            throw new IllegalArgumentException(cmd + " requires an index (e.g. '" + cmd + " 2')");
        try {
            int idx = Integer.parseInt(tokens[1]);
            if (idx <= 0) throw new NumberFormatException();
            return idx;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid index for '" + cmd + "': " + tokens[1]);
        }
    }

    private LocalDateTime parseDT(String text) {
        try {
            return LocalDateTime.parse(text, formatter); // "yyyy-MM-dd HH:mm" (no 'T')
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date-time '" + text +
                    "'. Expected format: yyyy-MM-dd HH:mm");
        }
    }
}