import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Brands } from './brands.model';
import { BrandsPopupService } from './brands-popup.service';
import { BrandsService } from './brands.service';

@Component({
    selector: 'jhi-brands-delete-dialog',
    templateUrl: './brands-delete-dialog.component.html'
})
export class BrandsDeleteDialogComponent {

    brands: Brands;

    constructor(
        private brandsService: BrandsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.brandsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'brandsListModification',
                content: 'Deleted an brands'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-brands-delete-popup',
    template: ''
})
export class BrandsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private brandsPopupService: BrandsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.brandsPopupService
                .open(BrandsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
