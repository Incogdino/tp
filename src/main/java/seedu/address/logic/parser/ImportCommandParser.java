package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.stream.Stream;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ImportCommand object.
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Returns true if any of the prefixes appears more than once.
     */
    private static boolean hasDuplicatePrefixes(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getAllValues(prefix).size() > 1);
    }

    @Override
    public ImportCommand parse(String args) throws ParseException {
        requireNonNull(args);

        // Tokenize the input arguments
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_PATH);

        // Check if the preamble is empty (no extra text before the first prefix)
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }

        // Check that the required prefix is present
        if (!arePrefixesPresent(argMultimap, CliSyntax.PREFIX_PATH)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }

        // Check for duplicate prefixes
        if (hasDuplicatePrefixes(argMultimap, CliSyntax.PREFIX_PATH)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }

        // Get the CSV file path
        String csvFilePath = argMultimap.getValue(CliSyntax.PREFIX_PATH).orElse("").trim();
        if (csvFilePath.isEmpty()) {
            throw new ParseException("CSV file path cannot be empty.");
        }

        return new ImportCommand(csvFilePath);
    }
}


