package com.project.port.adapter.semantic;

import java.util.Arrays;

import javax.annotation.Nonnull;

public enum OwlClassesVocabulary
{
    THERAPY("Therapy", "Therapy"),
    ARTIFACT_CLASS("ArtifactClass", "Classification"),
    TREATMENT_OPTION("TreatmentOption", "TreatmentOption"),

    ARTIFACT_TYPE("ArtifactType", "ArtifactType"),

    DRUGS("therapy_tree", "Drug"),
    SURGERIES("surgery_tree", "Surgery"),
    PLACEBOS("placebo_tree", "Placebo"),
    RADIOTHERAPIES("radiotherapy_tree", "Radiotherapy"),

    UNCLASSIFIED_DRUGS("UnclassifiedDrugs", "Unclassified"),
    UNCLASSIFIED_SURGERIES("UnclassifiedSurgeries", "Unclassified"),
    UNCLASSIFIED_PLACEBOS("UnclassifiedPlacebos", "Unclassified"),
    UNCLASSIFIED_RADIOTHERAPIES("UnclassifiedRadioTherapies", "Unclassified"),

    TQ("TQ", "Term query"),

    SYSTEM("System", "System");

    private final String abbreviatedIRI;

    private final String name;

    public String abbreviatedIRI()
    {
        return this.abbreviatedIRI;
    }

    public String getName()
    {
        return this.name;
    }

    OwlClassesVocabulary(String abbreviatedIRI, String name)
    {
        this.abbreviatedIRI = abbreviatedIRI;
        this.name = name;
    }

    public static OwlClassesVocabulary ofName(@Nonnull String name)
    {
        return Arrays.stream(values()).filter(p -> p.getName().equals(name)).findFirst().orElseThrow();
    }
}
