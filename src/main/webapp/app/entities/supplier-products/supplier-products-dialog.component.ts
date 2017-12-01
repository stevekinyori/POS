import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SupplierProducts } from './supplier-products.model';
import { SupplierProductsPopupService } from './supplier-products-popup.service';
import { SupplierProductsService } from './supplier-products.service';
import { Products, ProductsService } from '../products';
import { Suppliers, SuppliersService } from '../suppliers';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-supplier-products-dialog',
    templateUrl: './supplier-products-dialog.component.html'
})
export class SupplierProductsDialogComponent implements OnInit {

    supplierProducts: SupplierProducts;
    isSaving: boolean;

    products: Products[];

    suppliers: Suppliers[];
    firstItemDeliveryDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private supplierProductsService: SupplierProductsService,
        private productsService: ProductsService,
        private suppliersService: SuppliersService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.productsService
            .query({filter: 'suppliers-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.supplierProducts.products || !this.supplierProducts.products.id) {
                    this.products = res.json;
                } else {
                    this.productsService
                        .find(this.supplierProducts.products.id)
                        .subscribe((subRes: Products) => {
                            this.products = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.suppliersService
            .query({filter: 'products-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.supplierProducts.supplier || !this.supplierProducts.supplier.id) {
                    this.suppliers = res.json;
                } else {
                    this.suppliersService
                        .find(this.supplierProducts.supplier.id)
                        .subscribe((subRes: Suppliers) => {
                            this.suppliers = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.supplierProducts.id !== undefined) {
            this.subscribeToSaveResponse(
                this.supplierProductsService.update(this.supplierProducts));
        } else {
            this.subscribeToSaveResponse(
                this.supplierProductsService.create(this.supplierProducts));
        }
    }

    private subscribeToSaveResponse(result: Observable<SupplierProducts>) {
        result.subscribe((res: SupplierProducts) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SupplierProducts) {
        this.eventManager.broadcast({ name: 'supplierProductsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProductsById(index: number, item: Products) {
        return item.id;
    }

    trackSuppliersById(index: number, item: Suppliers) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-supplier-products-popup',
    template: ''
})
export class SupplierProductsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private supplierProductsPopupService: SupplierProductsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.supplierProductsPopupService
                    .open(SupplierProductsDialogComponent as Component, params['id']);
            } else {
                this.supplierProductsPopupService
                    .open(SupplierProductsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
