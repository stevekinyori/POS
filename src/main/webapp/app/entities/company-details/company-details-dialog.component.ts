import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { COMPANY_DETAILS } from './company-details.model';
import { COMPANY_DETAILSPopupService } from './company-details-popup.service';
import { COMPANY_DETAILSService } from './company-details.service';

@Component({
    selector: 'jhi-company-details-dialog',
    templateUrl: './company-details-dialog.component.html'
})
export class COMPANY_DETAILSDialogComponent implements OnInit {

    cOMPANY_DETAILS: COMPANY_DETAILS;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private cOMPANY_DETAILSService: COMPANY_DETAILSService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cOMPANY_DETAILS.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cOMPANY_DETAILSService.update(this.cOMPANY_DETAILS));
        } else {
            this.subscribeToSaveResponse(
                this.cOMPANY_DETAILSService.create(this.cOMPANY_DETAILS));
        }
    }

    private subscribeToSaveResponse(result: Observable<COMPANY_DETAILS>) {
        result.subscribe((res: COMPANY_DETAILS) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: COMPANY_DETAILS) {
        this.eventManager.broadcast({ name: 'cOMPANY_DETAILSListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-company-details-popup',
    template: ''
})
export class COMPANY_DETAILSPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cOMPANY_DETAILSPopupService: COMPANY_DETAILSPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cOMPANY_DETAILSPopupService
                    .open(COMPANY_DETAILSDialogComponent as Component, params['id']);
            } else {
                this.cOMPANY_DETAILSPopupService
                    .open(COMPANY_DETAILSDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
