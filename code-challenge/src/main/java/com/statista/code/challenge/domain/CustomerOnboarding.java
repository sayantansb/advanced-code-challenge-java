package com.statista.code.challenge.domain;

import lombok.Data;

@Data
public class CustomerOnboarding {

    private String name;
    private String leadId;
    private LeadType leadType;
    private LeadResult leadResult;
    private String opportunityId;
    private OpportunityResult opportunityResult;
    private String contractId;
    private ContractStatus contractStatus;
    private ContractType contractType;
    private OnboardingStatus onboardingStatus;
    private OnboardingType onboardingType;

    public CustomerOnboarding(String name, String leadId, LeadType leadType, LeadResult leadResult, String opportunityId, OpportunityResult opportunityResult, String contractId, ContractStatus contractStatus, ContractType contractType, OnboardingStatus onboardingStatus,OnboardingType onboardingType) {
        this.name = name;
        this.leadId = leadId;
        this.leadType = leadType;
        this.leadResult = leadResult;
        this.opportunityId = opportunityId;
        this.opportunityResult = opportunityResult;
        this.contractId = contractId;
        this.contractStatus = contractStatus;
        this.contractType = contractType;
        this.onboardingStatus = onboardingStatus;
        this.onboardingType = onboardingType;
    }

    public CustomerOnboarding() {
    }
}
