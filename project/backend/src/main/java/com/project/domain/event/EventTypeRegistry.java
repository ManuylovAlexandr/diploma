package com.project.domain.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.project.domain.classification.drug.Drug;
import com.project.domain.classification.drug.event.DrugAlternativeNamesChanged;
import com.project.domain.classification.drug.event.DrugCanonicalNameChanged;
import com.project.domain.classification.drug.event.DrugDescriptionChanged;
import com.project.domain.classification.drug.event.DrugTherapyClassChanged;
import com.project.domain.classification.drug.event.NewDrugAdded;
import com.project.domain.classification.placebo.event.NewPlaceboAdded;
import com.project.domain.classification.radiotherapy.event.NewRadiotherapyAdded;
import com.project.domain.classification.surgery.event.NewSurgeryAdded;
import com.project.domain.classification.therapyclass.event.AlternativeNamesChanged;
import com.project.domain.classification.therapyclass.event.CanonicalNameChanged;
import com.project.domain.classification.therapyclass.event.DescriptionChanged;
import com.project.domain.classification.therapyclass.event.NewAdded;
import com.project.domain.classification.therapyclass.event.SuperClassChanged;
import com.project.domain.classification.placebo.Placebo;
import com.project.domain.classification.placebo.event.PlaceboAlternativeNamesChanged;
import com.project.domain.classification.placebo.event.PlaceboCanonicalNameChanged;
import com.project.domain.classification.placebo.event.PlaceboDescriptionChanged;
import com.project.domain.classification.placebo.event.PlaceboParentClassChanged;
import com.project.domain.classification.radiotherapy.Radiotherapy;
import com.project.domain.classification.radiotherapy.event.RadiotherapyAlternativeNamesChanged;
import com.project.domain.classification.radiotherapy.event.RadiotherapyCanonicalNameChanged;
import com.project.domain.classification.radiotherapy.event.RadiotherapyDescriptionChanged;
import com.project.domain.classification.radiotherapy.event.RadiotherapyParentClassChanged;
import com.project.domain.classification.surgery.Surgery;
import com.project.domain.classification.surgery.event.SurgeryAlternativeNamesChanged;
import com.project.domain.classification.surgery.event.SurgeryCanonicalNameChanged;
import com.project.domain.classification.surgery.event.SurgeryDescriptionChanged;
import com.project.domain.classification.surgery.event.SurgeryParentClassChanged;
import com.project.domain.classification.therapyclass.TherapyClass;
import com.project.domain.termquery.TermQuery;
import com.project.domain.termquery.events.TermQueryCanonicalNameChanged;
import com.project.domain.termquery.events.TermQueryQueryChanged;
import com.project.domain.termquery.events.NewTermQueryAdded;
import com.project.domain.treatmentoption.TreatmentOption;
import com.project.domain.treatmentoption.event.NewTreatmentOptionAdded;
import com.project.domain.treatmentoption.event.TreatmentOptionAlternativeNamesChanged;
import com.project.domain.treatmentoption.event.TreatmentOptionCanonicalNameChanged;
import com.project.domain.treatmentoption.event.TreatmentOptionComponentsChanged;
import com.project.domain.treatmentoption.event.TreatmentOptionDescriptionChanged;
import com.project.domain.treatmentoption.event.TreatmentOptionRelationToComponentsChanged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EventTypeRegistry
{

    private static final Logger LOGGER = LoggerFactory.getLogger(EventTypeRegistry.class);

    public static final String TYPE_NAME_TEMPLATE = "%s.%s";

    private final Map<Class<?>, EntityEventsRegistry> entitiesRegistry = new HashMap<>();

    private final Map<Class<?>, String> entityAliasMap = new HashMap<>();

    private final Map<String, Class<?>> aliasEntityMap = new HashMap<>();

    private static final EventTypeRegistry INSTANCE = new EventTypeRegistry();

    private final Map<String, Class<? extends DomainEvent>> aliasToTypeMap = new HashMap<>();

    @Nonnull
    public static EventTypeRegistry instance()
    {
        return INSTANCE;
    }

    private EventTypeRegistry()
    {
        initRegistry();
    }

    @Nonnull
    public Class<? extends DomainEvent> typeOfName(@Nonnull String typeName)
    {
        return Optional.ofNullable(this.aliasToTypeMap.get(typeName))
            .orElseThrow(() -> new IllegalArgumentException("There is no event type " + typeName));
    }

    @Nonnull
    public String typeNameOf(@Nonnull Class<?> entityType, @Nonnull Class<? extends DomainEvent> eventType)
    {

        return Optional.ofNullable(this.entitiesRegistry.get(entityType))
            .map(entityTypeEvents -> entityTypeEvents.registeredEventNameOf(eventType))
            .orElseThrow(() -> unknownTypeClass(entityType));
    }

    @Nonnull
    public Set<Class<? extends DomainEvent>> eventTypesOf(@Nonnull Class<?> entityType)
    {
        return Optional.ofNullable(this.entitiesRegistry.get(entityType))
            .map(EntityEventsRegistry::registeredEvents)
            .orElseThrow(() -> unknownTypeClass(entityType));
    }

    @Nonnull
    public Set<String> eventTypeNamesOf(@Nonnull Class<?> entityType)
    {
        return Optional.ofNullable(this.entitiesRegistry.get(entityType))
            .map(EntityEventsRegistry::registeredEventsNames)
            .orElseThrow(() -> unknownTypeClass(entityType));
    }

    @Nonnull
    public String entityNameOf(@Nonnull Class<?> entityType)
    {
        return Optional.ofNullable(this.entityAliasMap.get(entityType))
            .orElseThrow(() -> unknownTypeClass(entityType));
    }

    @Nonnull
    public Class<?> entityTypeOfName(@Nonnull String entityTypeName)
    {
        return Optional.ofNullable(this.aliasEntityMap.get(entityTypeName))
            .orElseThrow(
                () -> new IllegalArgumentException("Unknown entity type name " + entityTypeName)
            );
    }

    private static IllegalArgumentException unknownTypeClass(Class<?> searchTypeClass)
    {
        return new IllegalArgumentException("Failed to determine the type of class " + searchTypeClass.getName());
    }

    private void initRegistry()
    {
        registerArtifactClassEvents();

        register(Drug.class, "Drug")
            .registerEvent(NewDrugAdded.class, "NewDrugAdded")
            .registerEvent(DrugCanonicalNameChanged.class, "DrugCanonicalNameChanged")
            .registerEvent(DrugAlternativeNamesChanged.class, "DrugAlternativeNamesChanged")
            .registerEvent(DrugDescriptionChanged.class, "DrugDescriptionChanged")
            .registerEvent(DrugTherapyClassChanged.class, "DrugTherapyClassChanged");

        register(Surgery.class, "Surgery")
            .registerEvent(NewSurgeryAdded.class, "NewSurgeryAdded")
            .registerEvent(SurgeryCanonicalNameChanged.class, "SurgeryCanonicalNameChanged")
            .registerEvent(SurgeryAlternativeNamesChanged.class, "SurgeryAlternativeNamesChanged")
            .registerEvent(SurgeryParentClassChanged.class, "SurgeryParentClassChanged")
            .registerEvent(SurgeryDescriptionChanged.class, "SurgeryDescriptionChanged");

        register(Placebo.class, "Placebo")
            .registerEvent(NewPlaceboAdded.class, "NewPlaceboAdded")
            .registerEvent(PlaceboCanonicalNameChanged.class, "PlaceboCanonicalNameChanged")
            .registerEvent(PlaceboAlternativeNamesChanged.class, "PlaceboAlternativeNamesChanged")
            .registerEvent(PlaceboParentClassChanged.class, "PlaceboParentClassChanged")
            .registerEvent(PlaceboDescriptionChanged.class, "PlaceboDescriptionChanged");

        register(Radiotherapy.class, "Radiotherapy")
            .registerEvent(NewRadiotherapyAdded.class, "NewRadiotherapyAdded")
            .registerEvent(RadiotherapyCanonicalNameChanged.class, "RadiotherapyCanonicalNameChanged")
            .registerEvent(RadiotherapyAlternativeNamesChanged.class, "RadiotherapyAlternativeNamesChanged")
            .registerEvent(RadiotherapyParentClassChanged.class, "RadiotherapyParentClassChanged")
            .registerEvent(RadiotherapyDescriptionChanged.class, "RadiotherapyDescriptionChanged");

        register(TermQuery.class, "TermQuery")
            .registerEvent(NewTermQueryAdded.class, "NewTermQueryAdded")
            .registerEvent(TermQueryCanonicalNameChanged.class, "TermQueryCanonicalNameChanged")
            .registerEvent(TermQueryQueryChanged.class, "TermQueryQueryChanged");

        registerMedialContextClassEvents();
        populateGlobalAliasMap();
    }

    public EntityEventsRegistry register(Class<?> entityType, String entityAlias)
    {
        this.entitiesRegistry.put(entityType, new EntityEventsRegistry(entityAlias));
        this.entityAliasMap.put(entityType, entityAlias);
        this.aliasEntityMap.put(entityAlias, entityType);
        return this.entitiesRegistry.get(entityType);
    }

    public void populateGlobalAliasMap()
    {
        this.aliasToTypeMap.clear();
        this.entitiesRegistry.forEach((k, v) -> this.aliasToTypeMap.putAll(v.aliasToTypeMap));

        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("Registered entities and events");
            this.entitiesRegistry.forEach((Class<?> entityClass, EntityEventsRegistry entityEvents) -> {
                LOGGER.trace("Entity: {} -> {}", entityClass.getCanonicalName(), this.entityAliasMap.get(entityClass));
                entityEvents.registeredEvents()
                    .forEach(registeredEvent ->
                        LOGGER.trace(
                            "with event: {} -> {}",
                            registeredEvent.getCanonicalName(),
                            entityEvents.registeredEventNameOf(registeredEvent)
                        )
                    );
            });
        }
    }

    private void registerArtifactClassEvents(
        )
    {
        register(TherapyClass.class, "TherapyClass")
            .registerEvent(NewAdded.class, "NewAdded")
            .registerEvent(AlternativeNamesChanged.class, "AlternativeNamesChanged")
            .registerEvent(CanonicalNameChanged.class, "CanonicalNameChanged")
            .registerEvent(DescriptionChanged.class, "DescriptionChanged")
            .registerEvent(SuperClassChanged.class, "SuperClassChanged");
    }

    private void registerMedialContextClassEvents()
    {
        register(TreatmentOption.class, "TreatmentOption")
            .registerEvent(NewTreatmentOptionAdded.class, "NewTreatmentOptionAdded")
            .registerEvent(TreatmentOptionCanonicalNameChanged.class, "TreatmentOptionCanonicalNameChanged")
            .registerEvent(TreatmentOptionAlternativeNamesChanged.class, "TreatmentOptionAlternativeNamesChanged")
            .registerEvent(TreatmentOptionComponentsChanged.class, "TreatmentOptionComponentsChanged")
            .registerEvent(
                TreatmentOptionRelationToComponentsChanged.class,
                "TreatmentOptionRelationToComponentsChanged"
            )
            .registerEvent(TreatmentOptionDescriptionChanged.class, "TreatmentOptionDescriptionChanged");
    }

    public static final class EntityEventsRegistry
    {

        private final String entityType;

        private final Map<Class<? extends DomainEvent>, String> typeToAliasMap;

        private final Map<String, Class<? extends DomainEvent>> aliasToTypeMap;

        private EntityEventsRegistry(String entityTypeAlias)
        {
            this.entityType = entityTypeAlias;
            this.typeToAliasMap = new HashMap<>();
            this.aliasToTypeMap = new HashMap<>();
        }

        public <S extends DomainEvent> EntityEventsRegistry registerEvent(Class<S> eventType, String eventAlias)
        {
            eventAlias = String.format(TYPE_NAME_TEMPLATE, this.entityType, eventAlias);
            if (this.aliasToTypeMap.containsKey(eventAlias))
            {
                throw new IllegalStateException(
                    String.format(
                        "Event type must be unique. Class %1$s and class %2$s have the same event value: %3$s",
                        eventType, this.aliasToTypeMap.get(eventAlias).getName(), eventAlias
                    )
                );
            }

            this.typeToAliasMap.put(eventType, eventAlias);
            this.aliasToTypeMap.put(eventAlias, eventType);
            return this;
        }

        public Set<Class<? extends DomainEvent>> registeredEvents()
        {
            return this.typeToAliasMap.keySet();
        }

        public Set<String> registeredEventsNames()
        {
            return this.aliasToTypeMap.keySet();
        }

        public <S extends DomainEvent> String registeredEventNameOf(Class<S> eventType)
        {
            return this.typeToAliasMap.get(eventType);
        }
    }
}
