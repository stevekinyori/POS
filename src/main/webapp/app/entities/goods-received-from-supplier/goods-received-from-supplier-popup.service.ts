import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { GoodsReceivedFromSupplier } from './goods-received-from-supplier.model';
import { GoodsReceivedFromSupplierService } from './goods-received-from-supplier.service';

@Injectable()
export class GoodsReceivedFromSupplierPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private goodsReceivedFromSupplierService: GoodsReceivedFromSupplierService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.goodsReceivedFromSupplierService.find(id).subscribe((goodsReceivedFromSupplier) => {
                    this.ngbModalRef = this.goodsReceivedFromSupplierModalRef(component, goodsReceivedFromSupplier);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.goodsReceivedFromSupplierModalRef(component, new GoodsReceivedFromSupplier());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    goodsReceivedFromSupplierModalRef(component: Component, goodsReceivedFromSupplier: GoodsReceivedFromSupplier): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.goodsReceivedFromSupplier = goodsReceivedFromSupplier;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
