import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Packaging } from './packaging.model';
import { PackagingPopupService } from './packaging-popup.service';
import { PackagingService } from './packaging.service';

@Component({
    selector: 'jhi-packaging-dialog',
    templateUrl: './packaging-dialog.component.html'
})
export class PackagingDialogComponent implements OnInit {

    packaging: Packaging;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private packagingService: PackagingService,
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
        if (this.packaging.id !== undefined) {
            this.subscribeToSaveResponse(
                this.packagingService.update(this.packaging));
        } else {
            this.subscribeToSaveResponse(
                this.packagingService.create(this.packaging));
        }
    }

    private subscribeToSaveResponse(result: Observable<Packaging>) {
        result.subscribe((res: Packaging) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Packaging) {
        this.eventManager.broadcast({ name: 'packagingListModification', content: 'OK'});
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
    selector: 'jhi-packaging-popup',
    template: ''
})
export class PackagingPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private packagingPopupService: PackagingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.packagingPopupService
                    .open(PackagingDialogComponent as Component, params['id']);
            } else {
                this.packagingPopupService
                    .open(PackagingDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
