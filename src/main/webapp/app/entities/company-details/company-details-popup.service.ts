import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { COMPANY_DETAILS } from './company-details.model';
import { COMPANY_DETAILSService } from './company-details.service';

@Injectable()
export class COMPANY_DETAILSPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private cOMPANY_DETAILSService: COMPANY_DETAILSService

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
                this.cOMPANY_DETAILSService.find(id).subscribe((cOMPANY_DETAILS) => {
                    cOMPANY_DETAILS.dateOpened = this.datePipe
                        .transform(cOMPANY_DETAILS.dateOpened, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.cOMPANY_DETAILSModalRef(component, cOMPANY_DETAILS);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.cOMPANY_DETAILSModalRef(component, new COMPANY_DETAILS());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    cOMPANY_DETAILSModalRef(component: Component, cOMPANY_DETAILS: COMPANY_DETAILS): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.cOMPANY_DETAILS = cOMPANY_DETAILS;
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
