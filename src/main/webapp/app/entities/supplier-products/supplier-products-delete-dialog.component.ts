import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SupplierProducts } from './supplier-products.model';
import { SupplierProductsPopupService } from './supplier-products-popup.service';
import { SupplierProductsService } from './supplier-products.service';

@Component({
    selector: 'jhi-supplier-products-delete-dialog',
    templateUrl: './supplier-products-delete-dialog.component.html'
})
export class SupplierProductsDeleteDialogComponent {

    supplierProducts: SupplierProducts;

    constructor(
        private supplierProductsService: SupplierProductsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.supplierProductsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'supplierProductsListModification',
                content: 'Deleted an supplierProducts'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-supplier-products-delete-popup',
    template: ''
})
export class SupplierProductsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private supplierProductsPopupService: SupplierProductsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.supplierProductsPopupService
                .open(SupplierProductsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
