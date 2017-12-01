import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { COMPANY_DETAILS } from './company-details.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class COMPANY_DETAILSService {

    private resourceUrl = SERVER_API_URL + 'api/company-details';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/company-details';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(cOMPANY_DETAILS: COMPANY_DETAILS): Observable<COMPANY_DETAILS> {
        const copy = this.convert(cOMPANY_DETAILS);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(cOMPANY_DETAILS: COMPANY_DETAILS): Observable<COMPANY_DETAILS> {
        const copy = this.convert(cOMPANY_DETAILS);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<COMPANY_DETAILS> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to COMPANY_DETAILS.
     */
    private convertItemFromServer(json: any): COMPANY_DETAILS {
        const entity: COMPANY_DETAILS = Object.assign(new COMPANY_DETAILS(), json);
        entity.dateOpened = this.dateUtils
            .convertDateTimeFromServer(json.dateOpened);
        return entity;
    }

    /**
     * Convert a COMPANY_DETAILS to a JSON which can be sent to the server.
     */
    private convert(cOMPANY_DETAILS: COMPANY_DETAILS): COMPANY_DETAILS {
        const copy: COMPANY_DETAILS = Object.assign({}, cOMPANY_DETAILS);

        copy.dateOpened = this.dateUtils.toDate(cOMPANY_DETAILS.dateOpened);
        return copy;
    }
}
