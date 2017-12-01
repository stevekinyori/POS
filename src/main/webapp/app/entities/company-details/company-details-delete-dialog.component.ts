import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { COMPANY_DETAILS } from './company-details.model';
import { COMPANY_DETAILSPopupService } from './company-details-popup.service';
import { COMPANY_DETAILSService } from './company-details.service';

@Component({
    selector: 'jhi-company-details-delete-dialog',
    templateUrl: './company-details-delete-dialog.component.html'
})
export class COMPANY_DETAILSDeleteDialogComponent {

    cOMPANY_DETAILS: COMPANY_DETAILS;

    constructor(
        private cOMPANY_DETAILSService: COMPANY_DETAILSService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cOMPANY_DETAILSService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cOMPANY_DETAILSListModification',
                content: 'Deleted an cOMPANY_DETAILS'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-company-details-delete-popup',
    template: ''
})
export class COMPANY_DETAILSDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cOMPANY_DETAILSPopupService: COMPANY_DETAILSPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.cOMPANY_DETAILSPopupService
                .open(COMPANY_DETAILSDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
