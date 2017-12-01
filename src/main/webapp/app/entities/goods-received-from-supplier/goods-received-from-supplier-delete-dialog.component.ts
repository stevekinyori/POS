import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { GoodsReceivedFromSupplier } from './goods-received-from-supplier.model';
import { GoodsReceivedFromSupplierPopupService } from './goods-received-from-supplier-popup.service';
import { GoodsReceivedFromSupplierService } from './goods-received-from-supplier.service';

@Component({
    selector: 'jhi-goods-received-from-supplier-delete-dialog',
    templateUrl: './goods-received-from-supplier-delete-dialog.component.html'
})
export class GoodsReceivedFromSupplierDeleteDialogComponent {

    goodsReceivedFromSupplier: GoodsReceivedFromSupplier;

    constructor(
        private goodsReceivedFromSupplierService: GoodsReceivedFromSupplierService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.goodsReceivedFromSupplierService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'goodsReceivedFromSupplierListModification',
                content: 'Deleted an goodsReceivedFromSupplier'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-goods-received-from-supplier-delete-popup',
    template: ''
})
export class GoodsReceivedFromSupplierDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private goodsReceivedFromSupplierPopupService: GoodsReceivedFromSupplierPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.goodsReceivedFromSupplierPopupService
                .open(GoodsReceivedFromSupplierDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
