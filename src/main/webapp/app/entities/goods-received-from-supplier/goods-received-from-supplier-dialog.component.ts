import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { GoodsReceivedFromSupplier } from './goods-received-from-supplier.model';
import { GoodsReceivedFromSupplierPopupService } from './goods-received-from-supplier-popup.service';
import { GoodsReceivedFromSupplierService } from './goods-received-from-supplier.service';
import { Packaging, PackagingService } from '../packaging';
import { Products, ProductsService } from '../products';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-goods-received-from-supplier-dialog',
    templateUrl: './goods-received-from-supplier-dialog.component.html'
})
export class GoodsReceivedFromSupplierDialogComponent implements OnInit {

    goodsReceivedFromSupplier: GoodsReceivedFromSupplier;
    isSaving: boolean;

    packs: Packaging[];

    batchproducts: Products[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private goodsReceivedFromSupplierService: GoodsReceivedFromSupplierService,
        private packagingService: PackagingService,
        private productsService: ProductsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.packagingService
            .query({filter: 'goodsreceivedfromsupplier-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.goodsReceivedFromSupplier.pack || !this.goodsReceivedFromSupplier.pack.id) {
                    this.packs = res.json;
                } else {
                    this.packagingService
                        .find(this.goodsReceivedFromSupplier.pack.id)
                        .subscribe((subRes: Packaging) => {
                            this.packs = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.productsService
            .query({filter: 'receivedfromsupplier-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.goodsReceivedFromSupplier.batchProduct || !this.goodsReceivedFromSupplier.batchProduct.id) {
                    this.batchproducts = res.json;
                } else {
                    this.productsService
                        .find(this.goodsReceivedFromSupplier.batchProduct.id)
                        .subscribe((subRes: Products) => {
                            this.batchproducts = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.goodsReceivedFromSupplier.id !== undefined) {
            this.subscribeToSaveResponse(
                this.goodsReceivedFromSupplierService.update(this.goodsReceivedFromSupplier));
        } else {
            this.subscribeToSaveResponse(
                this.goodsReceivedFromSupplierService.create(this.goodsReceivedFromSupplier));
        }
    }

    private subscribeToSaveResponse(result: Observable<GoodsReceivedFromSupplier>) {
        result.subscribe((res: GoodsReceivedFromSupplier) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: GoodsReceivedFromSupplier) {
        this.eventManager.broadcast({ name: 'goodsReceivedFromSupplierListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPackagingById(index: number, item: Packaging) {
        return item.id;
    }

    trackProductsById(index: number, item: Products) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-goods-received-from-supplier-popup',
    template: ''
})
export class GoodsReceivedFromSupplierPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private goodsReceivedFromSupplierPopupService: GoodsReceivedFromSupplierPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.goodsReceivedFromSupplierPopupService
                    .open(GoodsReceivedFromSupplierDialogComponent as Component, params['id']);
            } else {
                this.goodsReceivedFromSupplierPopupService
                    .open(GoodsReceivedFromSupplierDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
