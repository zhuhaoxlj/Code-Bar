package Snippets;

import javax.swing.*;

public class CodeSnippets {
    private String prefix;
    private String description;
    private String content;
    private String languageType;
    private String group;
    private ImageIcon prefixIcon;

    public ImageIcon getPrefixIcon() {
        return prefixIcon;
    }

    public void setPrefixIcon(ImageIcon prefixIcon) {
        this.prefixIcon = prefixIcon;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public CodeSnippets() {
    }

    CodeSnippets(String prefix, String content) {
        this.prefix = prefix;
        this.content = content;
    }

    CodeSnippets(String prefix, String content, String languageType) {
        this.prefix = prefix;
        this.content = content;
        this.languageType = languageType;
    }

    CodeSnippets(String prefix, String description, String content, String languageType) {
        this.prefix = prefix;
        this.description = description;
        this.content = content;
        this.languageType = languageType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }
}
