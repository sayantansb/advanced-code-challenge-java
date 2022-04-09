package com.statista.code.challenge.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.UUID;

@Data
@Slf4j
public abstract class Department {
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

    /**
     * The algorithm to do business and its sequence of steps do not change
     * Different Subclasses (for Automated/Manual sales processes) provide hooks for the steps which vary
     * This is the reason of introducing template method pattern
     * @return
     */

    public final CustomerOnboarding doBusiness(){
        createLead();
        convertLead();
        createOpportunity();
        closeOpportunity();
        createContract();
        sendContract();
        receiveSignedContract();
        performTechnicalOnboarding();
        return new CustomerOnboarding(name, leadId, leadType, leadResult, opportunityId, opportunityResult, contractId, contractStatus, contractType, onboardingStatus, onboardingType);
    }

    public abstract void createLead();
    public abstract void createContract();
    public abstract void performTechnicalOnboarding();


    public void convertLead(){
        switch((new Random().nextInt(10))%3 ){
            case 0 :
                this.setLeadResult(LeadResult.CREATED);
                break;
            case 1 :
                this.setLeadResult(LeadResult.CONVERTED);
                break;
            case 2 :
                this.setLeadResult(LeadResult.CANCELLED);
                break;
            default :
                this.setLeadResult(LeadResult.CREATED);
        }
    }

    public void createOpportunity(){
        if(this.leadResult==LeadResult.CONVERTED){
            this.setOpportunityId(UUID.randomUUID().toString());
        }else{
            this.setOpportunityId("NOT_APPLICABLE");
        }
    }

    public void closeOpportunity(){
        if(!this.opportunityId.equals("NOT_APPLICABLE")){
            switch((new Random().nextInt(10))%2 ){
                case 0 :
                    this.setOpportunityResult(OpportunityResult.CLOSED_WON);
                    break;
                case 1 :
                    this.setOpportunityResult(OpportunityResult.CLOSED_LOST);
                    break;
                default :
                    this.setOpportunityResult(OpportunityResult.CLOSED_WON);
            }
        }else{
            this.setOpportunityResult(OpportunityResult.NOT_INITIATED);
        }
    }

    public void sendContract(){
        if(!(this.contractId.equals("NOT_APPLICABLE"))){
           this.contractStatus = ContractStatus.SENT;
        }else{
            this.contractStatus = ContractStatus.NOT_APPLICABLE;
        }
    }

    public void receiveSignedContract(){
        if(!this.contractId.equals("NOT_APPLICABLE")){

            switch((new Random().nextInt(10))%2 ){
                case 0 :
                    this.setContractStatus(ContractStatus.ACCEPTED);
                    break;
                case 1 :
                    this.setContractStatus(ContractStatus.REJECTED);
                    break;
                default :
                    this.setContractStatus(ContractStatus.ACCEPTED);
            }
        }
    }




}
