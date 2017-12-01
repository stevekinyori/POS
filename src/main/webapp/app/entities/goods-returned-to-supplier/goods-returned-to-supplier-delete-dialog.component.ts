import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { GoodsReturnedToSupplier } from './goods-returned-to-supplier.model';
import { GoodsReturnedToSupplierPopupService } from './goods-returned-to-supplier-popup.service';
import { GoodsReturnedToSupplierService } from './goods-returned-to-supplier.service';

@Component({
    selector: 'jhi-goods-returned-to-supplier-delete-dialog',
    templateUrl: './goods-returned-to-supplier-delete-dialog.component.html'
})
export class GoodsReturnedToSupplierDeleteDialogComponent {

    goodsReturnedToSupplier: GoodsReturnedToSupplier;

    constructor(
        private goodsReturnedToSupplierService: GoodsReturnedToSupplierService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.goodsReturnedToSupplierService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'goodsReturnedToSupplierListModification',
                content: 'Deleted an goodsReturnedToSupplier'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-goods-returned-to-supplier-delete-popup',
    template: ''
})
export class GoodsReturnedToSupplierDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private goodsReturnedToSupplierPopupService: GoodsReturnedToSupplierPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.goodsReturnedToSupplierPopupService
                .open(GoodsReturnedToSupplierDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
