import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Packaging } from './packaging.model';
import { PackagingPopupService } from './packaging-popup.service';
import { PackagingService } from './packaging.service';

@Component({
    selector: 'jhi-packaging-delete-dialog',
    templateUrl: './packaging-delete-dialog.component.html'
})
export class PackagingDeleteDialogComponent {

    packaging: Packaging;

    constructor(
        private packagingService: PackagingService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.packagingService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'packagingListModification',
                content: 'Deleted an packaging'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-packaging-delete-popup',
    template: ''
})
export class PackagingDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private packagingPopupService: PackagingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.packagingPopupService
                .open(PackagingDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
