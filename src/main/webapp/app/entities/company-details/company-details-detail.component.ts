import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { COMPANY_DETAILS } from './company-details.model';
import { COMPANY_DETAILSService } from './company-details.service';

@Component({
    selector: 'jhi-company-details-detail',
    templateUrl: './company-details-detail.component.html'
})
export class COMPANY_DETAILSDetailComponent implements OnInit, OnDestroy {

    cOMPANY_DETAILS: COMPANY_DETAILS;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private cOMPANY_DETAILSService: COMPANY_DETAILSService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCOMPANY_DETAILS();
    }

    load(id) {
        this.cOMPANY_DETAILSService.find(id).subscribe((cOMPANY_DETAILS) => {
            this.cOMPANY_DETAILS = cOMPANY_DETAILS;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCOMPANY_DETAILS() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cOMPANY_DETAILSListModification',
            (response) => this.load(this.cOMPANY_DETAILS.id)
        );
    }
}
