package com.project.port.adapter.semantic;

public enum Namespaces
{
    SOURCE("https://ontology/project"),
    PREFIX(SOURCE.namespace + "#");

    private final String namespace;

    Namespaces(String namespace)
    {
        this.namespace = namespace;
    }

    public String namespace()
    {
        return this.namespace;
    }
}
