package com.project.port.adapter.semantic.resource;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class TermQueryInfo
{

    @JsonProperty("id")
    private String id;

    @JsonProperty("canonicalName")
    private String canonicalName;

    @JsonProperty("query")
    private String query;

    @JsonProperty("description")
    private String description;

    @JsonProperty("synonyms")

    private Set<String> synonyms = new LinkedHashSet<>();

    public TermQueryInfo id(String id)
    {
        this.id = id;
        return this;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public TermQueryInfo canonicalName(String canonicalName)
    {
        this.canonicalName = canonicalName;
        return this;
    }

    public String getCanonicalName()
    {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName)
    {
        this.canonicalName = canonicalName;
    }

    public TermQueryInfo query(String query)
    {
        this.query = query;
        return this;
    }

    public String getQuery()
    {
        return query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public TermQueryInfo description(String description)
    {
        this.description = description;
        return this;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public TermQueryInfo synonyms(Set<String> synonyms)
    {
        this.synonyms = synonyms;
        return this;
    }

    public TermQueryInfo addSynonymsItem(String synonymsItem)
    {
        if (this.synonyms == null)
        {
            this.synonyms = new LinkedHashSet<>();
        }
        this.synonyms.add(synonymsItem);
        return this;
    }

    public Set<String> getSynonyms()
    {
        return synonyms;
    }

    public void setSynonyms(Set<String> synonyms)
    {
        this.synonyms = synonyms;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        TermQueryInfo termQueryInfo = (TermQueryInfo) o;
        return Objects.equals(this.id, termQueryInfo.id) &&
            Objects.equals(this.canonicalName, termQueryInfo.canonicalName) &&
            Objects.equals(this.query, termQueryInfo.query) &&
            Objects.equals(this.description, termQueryInfo.description) &&
            Objects.equals(this.synonyms, termQueryInfo.synonyms);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(
            id,
            canonicalName,
            query,
            description,
            synonyms
        );
    }
}

