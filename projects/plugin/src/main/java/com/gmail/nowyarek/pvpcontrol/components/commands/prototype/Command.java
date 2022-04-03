package com.gmail.nowyarek.pvpcontrol.components.commands.prototype;

import com.google.common.collect.ImmutableSet;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Command {
    public final static CommandDefaults defaults = new CommandDefaults();
    protected final String name;
    @Nullable
    private String description;
    @Nullable
    private String[] arguments;
    @Nullable
    protected Command parentCommand;
    protected Set<String> requiredPermisions = new HashSet<>();
    protected final Set<Command> subcommands = new HashSet<>();

    public Command(String name) {
        this.name = name;
    }

    public Command(String name, String description) {
        this(name);
        this.description = description;
    }

    public Command(String name, String description, String[] arguments) {
        this(name, description);
        this.arguments = arguments;
    }

    /**
     * Runs the command with the given arguments.
     *
     * @param sender - command sender, may be a player or console.
     * @param args   - argument given to command.
     */
    public abstract void onCommand(CommandSender sender, String[] args, CommandContext commandContext);

    /**
     * Runs the command with the given arguments.
     *
     * @param sender - command sender, may be a player or console.
     * @param args   - argument given to command.
     * @return collection of autocomplete suggestions
     */
    public List<String> onTabComplete(CommandSender sender, String[] args, CommandContext commandContext) {
        return Collections.emptyList();
    }

    public String getName() {
        return this.name;
    }

    public String getNamespace() {
        @Nullable String parentCommandName = (this.parentCommand != null ? this.parentCommand.getNamespace() : null);
        return (parentCommandName != null ? parentCommandName + " " : "") + this.name;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public Optional<String[]> getArguments() {
        return Optional.ofNullable(this.arguments);
    }

    public void setArguments(@Nullable String[] arguments) {
        this.arguments = arguments;
    }

    public Optional<Command> getParentCommand() {
        return Optional.ofNullable(this.parentCommand);
    }

    public Optional<RootCommand> getRootCommand() {
        if (this instanceof RootCommand) return Optional.of((RootCommand) this);
        else if (this.parentCommand != null) return this.parentCommand.getRootCommand();
        else return Optional.empty();
    }

    protected void setParentCommand(Command command) {
        this.parentCommand = command;
    }

    protected void unsetParentCommand(Command command) {
        if (Objects.equals(this.parentCommand, command)) this.parentCommand = null;
    }

    public Set<String> getRequiredPermisionsSet() {
        return requiredPermisions;
    }

    public void setRequiredPermisions(Set<String> requiredPermisions) {
        this.requiredPermisions = requiredPermisions;
    }

    public void defineRequiredPermision(String permission) {
        this.requiredPermisions.add(permission);
    }

    public void undefineRequiredPermision(String permission) {
        this.requiredPermisions.remove(permission);
    }

    public Command addCommand(Command command) {
        command.setParentCommand(this);
        this.subcommands.add(command);
        return this;
    }

    public ImmutableSet<Command> getCommands() {
        return ImmutableSet.copyOf(this.subcommands);
    }

    public Optional<Command> getCommand(String name) {
        return this.subcommands.stream().filter((Command command) -> command.getName().equalsIgnoreCase(name)).findFirst();
    }

    public <T extends Command> T getTypedCommand(String name, Class<T> clazz) {
        Command foundCommand = this.subcommands.stream().filter((Command command) -> command.getName().equalsIgnoreCase(name)).findFirst().orElse(null);

        Objects.requireNonNull(foundCommand, String.format("Command with the specified name `%s` was not found.", name));
        return clazz.cast(foundCommand);
    }

    public boolean removeCommand(Command command) {
        command.unsetParentCommand(this);
        return this.subcommands.remove(command);
    }

    public Optional<Command> removeCommand(String name) {
        Optional<Command> command = this.getCommand(name);
        command.ifPresent(this::removeCommand);
        return command;
    }

    protected void parseCommandEvent(CommandSender sender, String[] args, CommandContext context) {
        boolean hasOneOfRequiredPermissions = this.requiredPermisions.size() <= 0 || this.requiredPermisions.stream().anyMatch(sender::hasPermission);

        if (!hasOneOfRequiredPermissions) {
            sender.sendMessage(Command.defaults.missingPermissionsMessage.get());
            return;
        }

        if (args.length > 0 && this.subcommands.size() > 0) {
            String subCommandName = args[0];
            Optional<Command> command = this.getCommand(subCommandName);

            if (command.isPresent()) {
                String[] newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                command.get().parseCommandEvent(sender, newArgs, context);
                return;
            }
        }

        try {
            this.onCommand(sender, args, context);
        } catch (Exception e) {
            throw new CommandExecutionException(this.name, e);
        }
    }

    protected List<String> parseTabCompleteEvent(CommandSender sender, String[] args, CommandContext context) {
        List<String> suggestions = new ArrayList<>();
        boolean hasOneOfRequiredPermissions = this.requiredPermisions.size() <= 0 || this.requiredPermisions.stream().anyMatch(sender::hasPermission);

        if (!hasOneOfRequiredPermissions) return suggestions;

        if (args.length > 0 && this.subcommands.size() > 0) {
            String hint = args[0];

            List<String> similarCommandsSuggestions = this.subcommands.stream()
                .filter((Command cmd) -> cmd.getName().startsWith(hint))
                .filter((Command cmd) -> cmd.getRequiredPermisionsSet().stream().anyMatch(sender::hasPermission))
                .map(Command::getName)
                .collect(Collectors.toList());

            suggestions.addAll(similarCommandsSuggestions);
        } else {
            suggestions.addAll(this.subcommands.stream()
                .filter((Command cmd) -> cmd.getRequiredPermisionsSet().stream().anyMatch(sender::hasPermission))
                .map(Command::getName)
                .collect(Collectors.toList()));
        }

        try {
            suggestions.addAll(this.onTabComplete(sender, args, context));
        } catch (Exception e) {
            throw new CommandAutoCompletationException(this.name, e);
        }

        return suggestions;
    }
}
