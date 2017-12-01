import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Suppliers } from './suppliers.model';
import { SuppliersPopupService } from './suppliers-popup.service';
import { SuppliersService } from './suppliers.service';
import { SupplierProducts, SupplierProductsService } from '../supplier-products';
import { GoodsReturnedToSupplier, GoodsReturnedToSupplierService } from '../goods-returned-to-supplier';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-suppliers-dialog',
    templateUrl: './suppliers-dialog.component.html'
})
export class SuppliersDialogComponent implements OnInit {

    suppliers: Suppliers;
    isSaving: boolean;

    supplierproducts: SupplierProducts[];

    goodsreturnedtosuppliers: GoodsReturnedToSupplier[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private suppliersService: SuppliersService,
        private supplierProductsService: SupplierProductsService,
        private goodsReturnedToSupplierService: GoodsReturnedToSupplierService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.supplierProductsService.query()
            .subscribe((res: ResponseWrapper) => { this.supplierproducts = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.goodsReturnedToSupplierService.query()
            .subscribe((res: ResponseWrapper) => { this.goodsreturnedtosuppliers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.suppliers.id !== undefined) {
            this.subscribeToSaveResponse(
                this.suppliersService.update(this.suppliers));
        } else {
            this.subscribeToSaveResponse(
                this.suppliersService.create(this.suppliers));
        }
    }

    private subscribeToSaveResponse(result: Observable<Suppliers>) {
        result.subscribe((res: Suppliers) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Suppliers) {
        this.eventManager.broadcast({ name: 'suppliersListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSupplierProductsById(index: number, item: SupplierProducts) {
        return item.id;
    }

    trackGoodsReturnedToSupplierById(index: number, item: GoodsReturnedToSupplier) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-suppliers-popup',
    template: ''
})
export class SuppliersPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private suppliersPopupService: SuppliersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.suppliersPopupService
                    .open(SuppliersDialogComponent as Component, params['id']);
            } else {
                this.suppliersPopupService
                    .open(SuppliersDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
