package com.statista.code.challenge.domain;

import java.util.Random;
import java.util.UUID;

public class AutomatedSalesDepartment extends Department{

    @Override
    public void createLead() {
        setLeadType(LeadType.AUTOMATED);
        setLeadId(UUID.randomUUID().toString());
    }

    @Override
    public void createContract() {
        if(getOpportunityResult()==OpportunityResult.CLOSED_WON){
            setContractType(ContractType.AUTOMATED);
            setContractStatus(ContractStatus.CREATED);
            setContractId(UUID.randomUUID().toString());
        }else{
            setContractType(ContractType.NOT_APPLICABLE);
            setContractStatus(ContractStatus.NOT_APPLICABLE);
            setContractId("NOT_APPLICABLE");
        }

    }

    @Override
    public void performTechnicalOnboarding() {
        if(getContractStatus()==ContractStatus.ACCEPTED){
            setOnboardingType(OnboardingType.AUTOMATED);
            switch((new Random().nextInt(10))%2 ){
                case 0 :
                    setOnboardingStatus(OnboardingStatus.COMPLETED);
                    break;
                case 1 :
                    setOnboardingStatus(OnboardingStatus.FAILED);
                    break;
                default :
                    setOnboardingStatus(OnboardingStatus.IN_PROGRESS);
            }
        }else{
            setOnboardingType(OnboardingType.NOT_APPLICABLE);
            setOnboardingStatus(OnboardingStatus.NOT_STARTED);
        }


    }
}
