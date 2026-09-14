package com.acme.cashflow.mrr;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

class PlanLookupFacadeTest {

    @Test
    void routesSinglePlanToV2WhenFlagIsEnabled() {
        LegacyPlanLookup legacy = mock(LegacyPlanLookup.class);
        PlanLookupV2 modern = mock(PlanLookupV2.class);
        MrrFeatureFlags flags = mock(MrrFeatureFlags.class);
        PlanInfo expected = new PlanInfo("pro-month", 99900L, "monthly");
        when(flags.useV2ForSinglePlan()).thenReturn(true);
        when(modern.find("pro-month")).thenReturn(expected);

        PlanLookupFacade facade = new PlanLookupFacade(legacy, modern, flags);

        assertSame(expected, facade.find("pro-month", true));
        verify(modern).find("pro-month");
        verifyNoInteractions(legacy);
    }

    @Test
    void routesToLegacyWhenFlagIsDisabled() {
        LegacyPlanLookup legacy = mock(LegacyPlanLookup.class);
        PlanLookupV2 modern = mock(PlanLookupV2.class);
        MrrFeatureFlags flags = mock(MrrFeatureFlags.class);
        PlanInfo expected = new PlanInfo("pro-month", 99900L, "monthly");
        when(flags.useV2ForSinglePlan()).thenReturn(false);
        when(legacy.find("pro-month")).thenReturn(expected);

        PlanLookupFacade facade = new PlanLookupFacade(legacy, modern, flags);

        assertSame(expected, facade.find("pro-month", true));
        verify(legacy).find("pro-month");
        verifyNoInteractions(modern);
    }

    @Test
    void routesMultiPlanToLegacyEvenWhenFlagIsEnabled() {
        LegacyPlanLookup legacy = mock(LegacyPlanLookup.class);
        PlanLookupV2 modern = mock(PlanLookupV2.class);
        MrrFeatureFlags flags = mock(MrrFeatureFlags.class);
        PlanInfo expected = new PlanInfo("pro-month", 99900L, "monthly");
        when(flags.useV2ForSinglePlan()).thenReturn(true);
        when(legacy.find("pro-month")).thenReturn(expected);

        PlanLookupFacade facade = new PlanLookupFacade(legacy, modern, flags);

        assertSame(expected, facade.find("pro-month", false));
        verify(legacy).find("pro-month");
        verifyNoInteractions(modern);
    }
}